import { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import {
  getTodayMeals,
  regenerateMeals,
  validateTodayMeals,
  getUserProfile,
  getToken,
} from '../services/api';
import MealCard from '../components/MealCard';
import './Home.css';

export default function Home() {
  const navigate = useNavigate();
  const [meals, setMeals] = useState([]);
  const [isValidated, setIsValidated] = useState(false);
  const [validatedAt, setValidatedAt] = useState(null);
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [validating, setValidating] = useState(false);

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

        const menuData = await getTodayMeals();
        setMeals(menuData.meals || []);
        setIsValidated(Boolean(menuData.validated));
        setValidatedAt(menuData.validatedAt || null);
      } catch (err) {
        console.error("Erreur chargement menu du jour", err);
      } finally {
        setLoading(false);
      }
    }

    loadData();
  }, []);

  const handleRegenerate = async () => {
    if (isValidated) {
      window.alert("Ce menu a déjà été validé pour aujourd'hui et ne peut plus être modifié.");
      return;
    }

    setRefreshing(true);
    try {
      const result = await regenerateMeals();
      setMeals(result.meals || []);
      setIsValidated(Boolean(result.validated));
      setValidatedAt(result.validatedAt || null);
    } catch (error) {
      if (error.response?.status === 409) {
        window.alert("Le menu du jour a déjà été validé et ne peut plus être régénéré.");
        setIsValidated(true);
        return;
      }

      if (error.response?.status === 401) {
        window.alert(
          'Vous devez être connecté pour régénérer votre menu.'
        );
        navigate('/login');
        return;
      }

      console.error("Erreur lors de la régénération du menu", error);
    } finally {
      setRefreshing(false);
    }
  };

  const handleValidate = async () => {
    if (!profile && !getToken()) {
      window.alert('Vous devez être connecté pour valider votre menu du jour.');
      navigate('/login');
      return;
    }

    if (isValidated) {
      return;
    }

    setValidating(true);
    try {
      const result = await validateTodayMeals();
      setMeals(result.meals || []);
      setIsValidated(true);
      setValidatedAt(result.validatedAt || null);

      window.dispatchEvent(
        new CustomEvent('regimenu:toast', {
          detail: {
            type: 'success',
            message: 'Votre menu du jour a été validé avec succès !',
          },
        }),
      );
    } catch (error) {
      if (error.response?.status === 409) {
        window.alert("Le menu du jour a déjà été validé.");
        setIsValidated(true);
        return;
      }

      if (error.response?.status === 401) {
        window.alert('Vous devez être connecté pour valider votre menu.');
        navigate('/login');
        return;
      }

      console.error("Erreur lors de la validation du menu", error);
    } finally {
      setValidating(false);
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

        {isValidated && (
          <div className="validation-status-banner">
            <span className="validation-badge">✓ Menu validé pour aujourd'hui</span>
            <p className="validation-info">
              Ce menu a été validé{validatedAt ? ` à ${new Date(validatedAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}` : ''}.
              Il est verrouillé et ne peut plus être modifié.
            </p>
          </div>
        )}

        {!isValidated ? (
          <div className="header-actions">
            <button 
              className="validate-btn"
              onClick={handleValidate}
              disabled={validating}
              title="Entériner votre choix du jour"
            >
              {validating ? (
                <>
                  <span className="btn-spinner"></span>
                  Validation en cours...
                </>
              ) : (
                "Valider mon menu du jour"
              )}
            </button>

            <button 
              className="regenerate-btn" 
              onClick={handleRegenerate} 
              disabled={refreshing}
              title="Générer d'autres suggestions"
            >
              {refreshing ? (
                <>
                  <span className="btn-spinner"></span>
                  Génération en cours...
                </>
              ) : (
                "Proposer d'autres repas"
              )}
            </button>
          </div>
        ) : (
          <div className="header-actions">
            <Link to="/historique" className="history-shortcut-btn">
              Consulter l'historique des repas
            </Link>
          </div>
        )}
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
