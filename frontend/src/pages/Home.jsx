import { useState, useEffect } from 'react';
import { getTodayMeals, regenerateMeals, getUserProfile } from '../services/api';
import MealCard from '../components/MealCard';
import './Home.css';

export default function Home() {
  const [meals, setMeals] = useState([]);
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  useEffect(() => {
    async function loadData() {
      try {
        let currentProfile = null;
        try {
          currentProfile = await getUserProfile();
          setProfile(currentProfile);
        } catch (err) {
          console.warn("Profil non récupéré (mode visiteur anonyme)", err);
        }

        const userId = currentProfile ? currentProfile.id : null;
        const data = await getTodayMeals(userId);
        setMeals(data);
      } catch (err) {
        console.error("Erreur chargement menu du jour", err);
      } finally {
        setLoading(false);
      }
    }

    loadData();
  }, []);

  const handleRegenerate = async () => {
    setRefreshing(true);
    try {
      const userId = profile ? profile.id : null;
      const newMeals = await regenerateMeals(userId);
      setMeals(newMeals);
    } catch (error) {
      console.error("Erreur lors de la régénération du menu", error);
    } finally {
      setRefreshing(false);
    }
  };

  const getActiveBadges = () => {
    if (!profile) return [];
    const badges = [];
    if (profile.isVegetarian) badges.push({ label: 'Végétarien', icon: '🥗' });
    if (profile.isVegan) badges.push({ label: 'Végan', icon: '🌱' });
    if (profile.isGlutenFree) badges.push({ label: 'Sans gluten', icon: '🌾' });
    if (profile.isLactoseFree) badges.push({ label: 'Sans lactose', icon: '🥛' });
    if (profile.isAllergyPeanut) badges.push({ label: 'Sans arachides', icon: '🥜' });
    if (profile.isAllergyDairy) badges.push({ label: 'Allergie lait', icon: '🥛' });
    if (profile.isAllergyGluten) badges.push({ label: 'Allergie gluten', icon: '🌾' });
    if (profile.isAllergyEgg) badges.push({ label: 'Sans œufs', icon: '🥚' });
    if (profile.isAllergySeafood) badges.push({ label: 'Sans fruits de mer', icon: '🦐' });
    return badges;
  };

  const activeBadges = getActiveBadges();

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
        <p>
          {profile
            ? `Suggestions personnalisées pour ${profile.username || 'votre profil'}.`
            : "Vos suggestions de repas du jour. Cliquez sur une recette pour voir les détails :"}
        </p>

        {activeBadges.length > 0 && (
          <div className="active-preferences-container">
            <span className="preferences-label">Préférences actives :</span>
            <div className="preferences-badges">
              {activeBadges.map((badge, index) => (
                <span key={index} className="preference-badge">
                  {badge.icon} {badge.label}
                </span>
              ))}
            </div>
          </div>
        )}

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
