import { useNavigate } from 'react-router-dom';
import './MealCard.css';

export default function MealCard({ meal }) {
  const navigate = useNavigate();

  return (
    <div className="meal-card" onClick={() => navigate(`/recipe/${meal.id}`)}>
      <div className="image-container">
        <img src={meal.imageUrl} alt={meal.title} className="meal-image" />
      </div>
      <div className="meal-info">
        <h3>{meal.title}</h3>
        <p>{meal.readyInMinutes} min</p>
        <div className="badges">
          {meal.isVegetarian && <span className="badge veggie">Végétarien</span>}
          {meal.isVegan && <span className="badge vegan">Végan</span>}
        </div>
      </div>
    </div>
  );
}