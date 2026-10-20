import { useEffect, useState } from 'react';
import {
  getUserProfile,
  updateUserProfile,
} from '../services/api';
import './Profile.css';

const INITIAL_PREFERENCES = {
  isAllergyPeanut: false,
  isAllergyDairy: false,
  isAllergyGluten: false,
  isAllergyEgg: false,
  isAllergySeafood: false,
  isVegetarian: false,
  isVegan: false,
  isGlutenFree: false,
  isLactoseFree: false,
};

// Liste des allergies (placée au début pour la sécurité)
const ALLERGY_OPTIONS = [
  { name: 'isAllergyPeanut', title: 'Arachides / Cacahuètes', description: 'Risque allergique grave.', icon: '🥜' },
  { name: 'isAllergyDairy', title: 'Lait / Produits laitiers', description: 'Intolérance ou allergie au lait.', icon: '🥛' },
  { name: 'isAllergyGluten', title: 'Gluten (Allergie / Cœliaque)', description: 'Ne tolère pas le blé, seigle, orge.', icon: '🌾' },
  { name: 'isAllergyEgg', title: 'Œufs', description: 'Éviter les œufs et dérivés.', icon: '🥚' },
  { name: 'isAllergySeafood', title: 'Poissons et Fruits de mer', description: 'Risque d’allergie aux produits de la mer.', icon: '🦐' },
];

// Liste des préférences de confort
const PREFERENCE_OPTIONS = [
  { name: 'isVegetarian', title: 'Végétarien', description: 'Sans viande ni poisson.' },
  { name: 'isVegan', title: 'Végan', description: 'Sans produits d’origine animale.' },
  { name: 'isGlutenFree', title: 'Sans gluten (Confort)', description: 'Évite les recettes contenant du gluten.' },
  { name: 'isLactoseFree', title: 'Sans lactose (Confort)', description: 'Évite les recettes contenant du lactose.' },
];

export default function Profile() {
  const [profile, setProfile] = useState(null);
  const [preferences, setPreferences] = useState(INITIAL_PREFERENCES);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    const loadProfile = async () => {
      try {
        setLoading(true);
        setError('');
        const data = await getUserProfile();
        setProfile(data);
        setPreferences({
          isAllergyPeanut: data.isAllergyPeanut,
          isAllergyDairy: data.isAllergyDairy,
          isAllergyGluten: data.isAllergyGluten,
          isAllergyEgg: data.isAllergyEgg,
          isAllergySeafood: data.isAllergySeafood,
          isVegetarian: data.isVegetarian,
          isVegan: data.isVegan,
          isGlutenFree: data.isGlutenFree,
          isLactoseFree: data.isLactoseFree,
        });
      } catch (err) {
        console.error(err);
        setError('Impossible de charger votre profil. Vérifiez que le backend est démarré.');
      } finally {
        setLoading(false);
      }
    };
    loadProfile();
  }, []);

  const handleChange = (event) => {
    const { name, checked } = event.target;
    setPreferences((current) => ({
      ...current,
      [name]: checked,
    }));
    setSuccess('');
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      setSaving(true);
      setError('');
      setSuccess('');
      const updatedProfile = await updateUserProfile(preferences);
      setProfile(updatedProfile);
      setPreferences({
        isAllergyPeanut: updatedProfile.isAllergyPeanut,
        isAllergyDairy: updatedProfile.isAllergyDairy,
        isAllergyGluten: updatedProfile.isAllergyGluten,
        isAllergyEgg: updatedProfile.isAllergyEgg,
        isAllergySeafood: updatedProfile.isAllergySeafood,
        isVegetarian: updatedProfile.isVegetarian,
        isVegan: updatedProfile.isVegan,
        isGlutenFree: updatedProfile.isGlutenFree,
        isLactoseFree: updatedProfile.isLactoseFree,
      });
      setSuccess('Vos informations ont bien été enregistrées.');
    } catch (err) {
      console.error(err);
      setError('Impossible d’enregistrer vos modifications. Vérifiez que le backend est démarré.');
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <section className="profile-page">
        <div className="profile-card profile-loading">
          <p>Chargement de votre profil...</p>
        </div>
      </section>
    );
  }

  return (
    <section className="profile-page">
      <div className="profile-card">
        <header className="profile-header">
          <p className="profile-kicker">Mon profil</p>
          <h1>Mes allergies et préférences alimentaires</h1>
          <p>Indiquez d'abord vos allergies pour votre sécurité, puis vos choix de préférences. Vous pourrez modifier ces choix à tout moment.</p>
        </header>

        {error && <div className="profile-message profile-message-error" role="alert">{error}</div>}
        {success && <div className="profile-message profile-message-success" role="status">{success}</div>}

        {profile && (
          <div className="profile-information">
            <div><span className="profile-label">Utilisateur</span><strong>{profile.username}</strong></div>
            <div><span className="profile-label">Email</span><strong>{profile.email}</strong></div>
          </div>
        )}

        <form className="preferences-form" onSubmit={handleSubmit}>
          
          {/* SECTION 1 : ALLERGIES */}
          <div className="allergies-section" style={{ backgroundColor: '#fff5f5', border: '2px solid #feb2b2', padding: '20px', borderRadius: '8px', marginBottom: '24px' }}>
            <h2 style={{ color: '#c53030', marginTop: 0 }}>🚨 1. Mes Allergies (Sécurité)</h2>
            <p style={{ color: '#742a2a', marginBottom: '15px' }}>Cochez les ingrédients qui provoquent des réactions chez vous :</p>
            <div className="preferences-list">
              {ALLERGY_OPTIONS.map((option) => (
                <label className="preference-option" key={option.name} style={{ backgroundColor: '#fff', padding: '10px', borderRadius: '6px', marginBottom: '8px', display: 'flex', alignItems: 'center' }}>
                  <input
                    type="checkbox"
                    name={option.name}
                    checked={preferences[option.name]}
                    onChange={handleChange}
                    style={{ transform: 'scale(1.3)', marginRight: '12px' }}
                  />
                  <span className="preference-text">
                    <strong style={{ fontSize: '1.05rem' }}>{option.icon} {option.title}</strong>
                    <span style={{ display: 'block', color: '#666', fontSize: '0.9rem' }}>{option.description}</span>
                  </span>
                </label>
              ))}
            </div>
          </div>

          {/* SECTION 2 : PRÉFÉRENCES */}
          <div className="comfort-section">
            <h2>🥗 2. Mes Préférences (Style de vie)</h2>
            <div className="preferences-list">
              {PREFERENCE_OPTIONS.map((option) => (
                <label className="preference-option" key={option.name}>
                  <input
                    type="checkbox"
                    name={option.name}
                    checked={preferences[option.name]}
                    onChange={handleChange}
                  />
                  <span className="preference-text">
                    <strong>{option.title}</strong>
                    <span>{option.description}</span>
                  </span>
                </label>
              ))}
            </div>
          </div>

          <button className="save-preferences-button" type="submit" disabled={saving}>
            {saving ? 'Enregistrement...' : 'Enregistrer mes choix'}
          </button>
        </form>
      </div>
    </section>
  );
}