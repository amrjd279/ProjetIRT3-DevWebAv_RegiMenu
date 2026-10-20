import { useState, useEffect } from 'react';
import { getTodayMeals, regenerateMeals } from '../services/api';
import MealCard from '../components/MealCard';
import './Home.css';

export default function Home() {
  const [meals, setMeals] = useState([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  useEffect(() => {
    // Charge le menu du jour persistant en base de données (ne change pas au refresh ou au changement d'onglet)
    getTodayMeals()
      .then((data) => {
        setMeals(data);
        setLoading(false);
      })
      .catch(() => setLoading(false));
  }, []);

  const handleRegenerate = async () => {
    setRefreshing(true);
    try {
      const newMeals = await regenerateMeals();
      setMeals(newMeals);
    } catch (error) {
      console.error("Erreur lors de la régénération du menu", error);
    } finally {
      setRefreshing(false);
    }
  };

  if (loading) {
    return (
      <div className="loader-container">
        <div className="spinner"></div>
        <p>Chargement des propositions du jour...</p>
      </div>
    );
  }

  return (
    <div className="home-container">
      <div className="home-header">
        <h1>Menu du Jour</h1>
        <p>Vos suggestions personnalisées du jour. Cliquez sur une recette pour voir les détails :</p>
        
        <div className="header-actions">
          <button 
            className="regenerate-btn" 
            onClick={handleRegenerate} 
            disabled={refreshing}
          >
            {refreshing ? (
              <>
                <span className="btn-spinner"></span>
                Génération en cours...
              </>
            ) : (
              "🎲 Proposer d'autres repas"
            )}
          </button>
        </div>
      </div>
      
      <div className="meals-grid">
        {meals.map((meal, index) => (
          <div key={meal.id || index} className="proposition-card-wrapper">
            <div className="proposition-tag">Repas #{index + 1}</div>
            <MealCard meal={meal} />
          </div>
        ))}
      </div>
    </div>
  );
}