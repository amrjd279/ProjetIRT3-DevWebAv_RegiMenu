import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { addFavorite, getFavorites, removeFavorite } from '../services/api';
import './MealCard.css';

export default function MealCard({ meal, onFavoriteChange }) {
  const navigate = useNavigate();
  const [favorite, setFavorite] = useState(false);
  const [savingFavorite, setSavingFavorite] = useState(false);

  useEffect(() => {
    let active = true;
    getFavorites()
      .then((favorites) => {
        if (active) {
          setFavorite(favorites.some((item) => String(item.id) === String(meal.id)));
        }
      })
      .catch((error) => {
        console.error('Impossible de charger les favoris.', error);
      });

    return () => {
      active = false;
    };
  }, [meal.id]);

  const handleFavorite = async (event) => {
    event.stopPropagation();
    if (savingFavorite) return;

    setSavingFavorite(true);
    try {
      if (favorite) {
        await removeFavorite(meal.id);
      } else {
        await addFavorite(meal);
      }
      const nextFavorite = !favorite;
      setFavorite(nextFavorite);
      onFavoriteChange?.(meal.id, nextFavorite);
    } catch (error) {
      console.error('Impossible de modifier le favori.', error);
    } finally {
      setSavingFavorite(false);
    }
  };

  return (
    <div className="meal-card" onClick={() => navigate(`/recipe/${meal.id}`)}>
      <div className="image-container">
        <img src={meal.imageUrl} alt={meal.title} className="meal-image" />
      </div>
      <div className="meal-info">
        <button
          type="button"
          className={`favorite-button ${favorite ? 'is-favorite' : ''}`}
          onClick={handleFavorite}
          disabled={savingFavorite}
          aria-label={favorite ? 'Retirer des favoris' : 'Ajouter aux favoris'}
        >
          {favorite ? '♥ Favori' : '♡ Ajouter aux favoris'}
        </button>
        <h3>{meal.title}</h3>
        <p className="meal-time">{meal.readyInMinutes} min</p>
        <div className="badges">
          {meal.isVegetarian && <span className="badge veggie">🌱 Végétarien</span>}
          {meal.isVegan && <span className="badge vegan">🌿 Végan</span>}
        </div>
      </div>
    </div>
  );
}