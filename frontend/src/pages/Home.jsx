import { useState, useEffect } from 'react';
import { getRandomMeals } from '../services/api';
import MealCard from '../components/MealCard';
import './Home.css';

export default function Home() {
  const [meals, setMeals] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getRandomMeals()
      .then((data) => {
        setMeals(data);
        setLoading(false);
      })
      .catch(() => setLoading(false));
  }, []);

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
        <h1>Proposition de Menus Aléatoires</h1>
        <p>Découvrez vos suggestions du jour en tant que visiteur :</p>
      </div>
      
      <div className="meals-grid">
        {meals.map((meal, index) => (
          <div key={meal.id || index} className="proposition-card-wrapper">
            <div className="proposition-tag">Proposition #{index + 1}</div>
            <MealCard meal={meal} />
          </div>
        ))}
      </div>
    </div>
  );
}