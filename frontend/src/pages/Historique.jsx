import { useEffect, useState } from 'react';
import MealCard from '../components/MealCard';
import { clearHistory, getHistory } from '../services/storage';
import './SavedMeals.css';

export default function Historique() {
  const [history, setHistory] = useState([]);

  useEffect(() => {
    const refresh = () => setHistory(getHistory());
    refresh();
    window.addEventListener('storage', refresh);
    return () => window.removeEventListener('storage', refresh);
  }, []);

  return (
    <div className="saved-meals-page">
      <div className="saved-meals-header">
        <h1>Historique des recettes</h1>
        <p>Les recettes consultées récemment sont conservées sur cet appareil.</p>
      </div>
      {history.length > 0 && (
        <div className="saved-meals-actions">
          <button type="button" onClick={() => {
            clearHistory();
            setHistory([]);
          }}>
            Effacer l’historique
          </button>
        </div>
      )}
      {history.length === 0 ? (
        <div className="empty-state">Aucune recette consultée pour le moment.</div>
      ) : (
        <div className="saved-meals-grid">
          {history.map((meal) => <MealCard meal={meal} key={meal.id} />)}
        </div>
      )}
    </div>
  );
}