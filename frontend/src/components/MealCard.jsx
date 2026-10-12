import { useNavigate } from 'react-router-dom';
import { isFavorite, toggleFavorite } from '../services/storage';
import './MealCard.css';

export default function MealCard({ meal, onFavoriteChange }) {
  const navigate = useNavigate();

  const handleFavorite = (event) => {
    event.stopPropagation();
    const favorite = toggleFavorite(meal);
    onFavoriteChange?.(meal.id, favorite);
  };

  return (
    <div className="meal-card" onClick={() => navigate(`/recipe/${meal.id}`)}>
      <div className="image-container">
        <img src={meal.imageUrl} alt={meal.title} className="meal-image" />
      </div>
      <div className="meal-info">
        <button
          type="button"
          className={`favorite-button ${isFavorite(meal.id) ? 'is-favorite' : ''}`}
          onClick={handleFavorite}
          aria-label={isFavorite(meal.id) ? 'Retirer des favoris' : 'Ajouter aux favoris'}
        >
          {isFavorite(meal.id) ? '♥ Favori' : '♡ Ajouter aux favoris'}
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