import { useEffect, useState } from 'react';
import MealCard from '../components/MealCard';
import { getFavorites, removeFavorite } from '../services/storage';
import './SavedMeals.css';

export default function Favoris() {
  const [favorites, setFavorites] = useState([]);

  useEffect(() => {
    const refresh = () => setFavorites(getFavorites());
    refresh();
    window.addEventListener('storage', refresh);
    return () => window.removeEventListener('storage', refresh);
  }, []);

  const handleFavoriteChange = (id, favorite) => {
    if (!favorite) {
      setFavorites((current) => current.filter((meal) => String(meal.id) !== String(id)));
    }
  };

  return (
    <div className="saved-meals-page">
      <div className="saved-meals-header">
        <h1>Mes recettes favorites</h1>
        <p>Retrouve ici les recettes que tu souhaites garder sous la main.</p>
      </div>
      {favorites.length === 0 ? (
        <div className="empty-state">Aucune recette favorite pour le moment.</div>
      ) : (
        <div className="saved-meals-grid">
          {favorites.map((meal) => (
            <div className="saved-meal-item" key={meal.id}>
              <MealCard meal={meal} onFavoriteChange={handleFavoriteChange} />
              <button type="button" className="remove-button" onClick={() => {
                removeFavorite(meal.id);
                setFavorites((current) => current.filter((item) => item.id !== meal.id));
              }}>
                Retirer des favoris
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}