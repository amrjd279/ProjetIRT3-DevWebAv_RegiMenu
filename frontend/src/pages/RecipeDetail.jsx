import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getRecipeDetails } from '../services/api';
import './RecipeDetail.css';

export default function RecipeDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [recipe, setRecipe] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getRecipeDetails(id)
      .then((data) => {
        setRecipe(data);
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
              recipe.instructions.split('\n').map((step, idx) => (
                <p key={idx}>{step}</p>
              ))
            ) : (
              <p>Aucune instruction détaillée disponible.</p>
            )}
          </div>
        </section>
      </div>
    </div>
  );
}