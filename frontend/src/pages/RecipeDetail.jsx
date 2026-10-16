import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getRecipeDetails } from '../services/api';
import { addToHistory, isFavorite, toggleFavorite } from '../services/storage';
import './RecipeDetail.css';

function parseInstructions(instructions) {
  if (!instructions) {
    return [];
  }

  const parsedDocument = new DOMParser().parseFromString(instructions, 'text/html');
  const cleanStep = (step) => step
    .replace(/\s+/g, ' ')
    .trim()
    .replace(/^[•●▪◦*-]\s*/, '')
    .replace(/^\d+[.)]\s*/, '');

  const listItems = Array.from(parsedDocument.querySelectorAll('ol, ul'))
    .flatMap((list) => Array.from(list.children)
      .filter((item) => item.tagName === 'LI'))
    .map((item) => cleanStep(item.textContent))
    .filter(Boolean);

  if (listItems.length > 0) {
    return listItems;
  }

  const paragraphs = Array.from(parsedDocument.querySelectorAll('p'))
    .map((paragraph) => cleanStep(paragraph.textContent))
    .filter(Boolean);

  if (paragraphs.length > 0) {
    return paragraphs;
  }

  return (parsedDocument.body.textContent || instructions)
    .split(/\r?\n/)
    .map(cleanStep)
    .filter(Boolean);
}

export default function RecipeDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [recipe, setRecipe] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getRecipeDetails(id)
      .then((data) => {
        setRecipe(data);
        addToHistory(data);
        setLoading(false);
      })
      .catch(() => setLoading(false));
  }, [id]);

  if (loading) {
    return (
      <div className="loader-container">
        <div className="spinner"></div>
        <p>Chargement des détails de la recette...</p>
      </div>
    );
  }

  if (!recipe) {
    return (
      <div className="loader-container">
        <h2>Recette introuvable</h2>
        <button className="back-btn" onClick={() => navigate('/')}>Retour à l'accueil</button>
      </div>
    );
  }

  return (
    <div className="recipe-detail-container">
      <button className="back-btn" onClick={() => navigate('/')}>
        ← Retour aux propositions
      </button>

      <div className="recipe-header">
        <img src={recipe.imageUrl} alt={recipe.title} className="recipe-detail-image" />
        <div className="recipe-header-info">
          <h1>{recipe.title}</h1>
          <div className="recipe-meta-badges">
            <span className="meta-tag">⏱️ {recipe.readyInMinutes} min</span>
            {recipe.servings && <span className="meta-tag">👥 {recipe.servings} portions</span>}
            {recipe.isGlutenFree && <span className="badge gluten-free">Sans Gluten</span>}
            {recipe.isLactoseFree && <span className="badge lactose-free">Sans Lactose</span>}
            {recipe.isVegetarian && <span className="badge veggie">Végétarien</span>}
            {recipe.isVegan && <span className="badge vegan">Végan</span>}
          </div>
          <button
            type="button"
            className={`favorite-button ${recipe && isFavorite(recipe.id) ? 'is-favorite' : ''}`}
            onClick={() => {
              toggleFavorite(recipe);
              setRecipe((current) => ({ ...current }));
            }}
          >
            {isFavorite(recipe.id) ? '♥ Retirer des favoris' : '♡ Ajouter aux favoris'}
          </button>
        </div>
      </div>

      <div className="recipe-content-grid">
        <section className="ingredients-section">
          <h2>Ingrédients</h2>
          <ul>
            {recipe.extendedIngredients?.map((ing, idx) => (
              <li key={ing.id || idx}>{ing.original}</li>
            ))}
          </ul>
        </section>

        <section className="instructions-section">
          <h2>Instructions de préparation</h2>
          <div className="instructions-text">
            {recipe.instructions ? (
              <ol>
                {parseInstructions(recipe.instructions).map((step, idx) => (
                  <li key={`${step}-${idx}`}>{step}</li>
                ))}
              </ol>
            ) : (
              <p>Aucune instruction détaillée disponible.</p>
            )}
          </div>
        </section>
      </div>
    </div>
  );
}