import { useEffect, useState } from 'react';

import {
  getUserProfile,
  updateUserProfile,
} from '../services/api';

import './Profile.css';

/**
 * Valeurs par défaut.
 *
 * Au départ, aucune préférence n'est sélectionnée.
 */
const INITIAL_PREFERENCES = {
  isVegetarian: false,
  isVegan: false,
  isGlutenFree: false,
  isLactoseFree: false,
};

/**
 * Liste des choix affichés dans le formulaire.
 */
const PREFERENCE_OPTIONS = [

  {
    name: 'isVegetarian',

    title: 'Végétarien',

    description:
      'Sans viande ni poisson.',
  },

  {
    name: 'isVegan',

    title: 'Végan',

    description:
      'Sans produits d’origine animale.',
  },

  {
    name: 'isGlutenFree',

    title: 'Sans gluten',

    description:
      'Évite les recettes contenant du gluten.',
  },

  {
    name: 'isLactoseFree',

    title: 'Sans lactose',

    description:
      'Évite les recettes contenant du lactose.',
  },

];


/**
 * Page Profile.
 *
 * US4.3 :
 * formulaire React permettant
 * de choisir les préférences alimentaires.
 */
export default function Profile() {

  /**
   * Profil récupéré depuis le backend.
   */
  const [profile, setProfile] =
    useState(null);

  /**
   * Préférences actuellement sélectionnées
   * dans le formulaire.
   */
  const [preferences, setPreferences] =
    useState(INITIAL_PREFERENCES);

  /**
   * Indique si le GET est en cours.
   */
  const [loading, setLoading] =
    useState(true);

  /**
   * Indique si le PUT est en cours.
   */
  const [saving, setSaving] =
    useState(false);

  /**
   * Message d'erreur.
   */
  const [error, setError] =
    useState('');

  /**
   * Message de succès.
   */
  const [success, setSuccess] =
    useState('');


  /**
   * =====================================================
   * CHARGEMENT DU PROFIL
   * =====================================================
   */
  useEffect(() => {

    const loadProfile =
      async () => {

        try {

          setLoading(true);

          setError('');

          /*
           * Appel :
           *
           * GET /api/profile
           */
          const data =
            await getUserProfile();

          /*
           * On sauvegarde le profil.
           */
          setProfile(data);

          /*
           * On initialise les cases
           * avec les valeurs de la base.
           */
          setPreferences({

            isVegetarian:
              data.isVegetarian,

            isVegan:
              data.isVegan,

            isGlutenFree:
              data.isGlutenFree,

            isLactoseFree:
              data.isLactoseFree,
          });

        } catch (err) {

          console.error(err);

          setError(
            'Impossible de charger votre profil. Vérifiez que le backend est démarré.'
          );

        } finally {

          setLoading(false);
        }
      };

    loadProfile();

  }, []);


  /**
   * =====================================================
   * MODIFICATION D'UNE CASE
   * =====================================================
   */
  const handleChange =
    (event) => {

      /*
       * name = nom de la case.
       *
       * checked = true ou false.
       */
      const {
        name,
        checked
      } = event.target;

      /*
       * On conserve les autres préférences
       * et on modifie seulement celle sélectionnée.
       */
      setPreferences(
        (current) => ({

          ...current,

          [name]: checked,

        })
      );

      /*
       * Le message de succès disparaît
       * dès qu'on modifie quelque chose.
       */
      setSuccess('');
    };


  /**
   * =====================================================
   * ENREGISTREMENT
   * =====================================================
   */
  const handleSubmit =
    async (event) => {

      /*
       * Empêche le rechargement
       * de la page.
       */
      event.preventDefault();

      try {

        setSaving(true);

        setError('');

        setSuccess('');

        /*
         * Appel :
         *
         * PUT /api/profile
         *
         * avec les préférences.
         */
        const updatedProfile =
          await updateUserProfile(
            preferences
          );

        /*
         * On actualise le profil.
         */
        setProfile(
          updatedProfile
        );

        /*
         * On actualise les cases.
         */
        setPreferences({

          isVegetarian:
            updatedProfile.isVegetarian,

          isVegan:
            updatedProfile.isVegan,

          isGlutenFree:
            updatedProfile.isGlutenFree,

          isLactoseFree:
            updatedProfile.isLactoseFree,

        });

        /*
         * Message de confirmation.
         */
        setSuccess(
          'Vos préférences ont bien été enregistrées.'
        );

      } catch (err) {

        console.error(err);

        setError(
          'Impossible d’enregistrer vos préférences. Vérifiez que le backend est démarré.'
        );

      } finally {

        setSaving(false);
      }
    };


  /**
   * =====================================================
   * CHARGEMENT
   * =====================================================
   */
  if (loading) {

    return (

      <section className="profile-page">

        <div
          className="profile-card profile-loading"
        >

          <p>
            Chargement de votre profil...
          </p>

        </div>

      </section>
    );
  }


  /**
   * =====================================================
   * AFFICHAGE
   * =====================================================
   */
  return (

    <section className="profile-page">

      <div className="profile-card">

        {/* En-tête */}
        <header className="profile-header">

          <p className="profile-kicker">
            Mon profil
          </p>

          <h1>
            Mes préférences alimentaires
          </h1>

          <p>
            Choisissez les aliments que vous
            souhaitez éviter ou privilégier.
            Vous pourrez modifier ces choix
            à tout moment.
          </p>

        </header>


        {/* Message erreur */}
        {error && (

          <div
            className="profile-message profile-message-error"
            role="alert"
          >
            {error}
          </div>

        )}


        {/* Message succès */}
        {success && (

          <div
            className="profile-message profile-message-success"
            role="status"
          >
            {success}
          </div>

        )}


        {/* Informations utilisateur */}
        {profile && (

          <div className="profile-information">

            <div>

              <span className="profile-label">
                Utilisateur
              </span>

              <strong>
                {profile.username}
              </strong>

            </div>


            <div>

              <span className="profile-label">
                Email
              </span>

              <strong>
                {profile.email}
              </strong>

            </div>

          </div>

        )}


        {/* Formulaire */}
        <form
          className="preferences-form"
          onSubmit={handleSubmit}
        >

          <h2>
            Mes choix
          </h2>


          <div className="preferences-list">

            {PREFERENCE_OPTIONS.map(
              (option) => (

                <label
                  className="preference-option"
                  key={option.name}
                >

                  <input
                    type="checkbox"
                    name={option.name}
                    checked={
                      preferences[
                        option.name
                      ]
                    }
                    onChange={handleChange}
                  />


                  <span className="preference-text">

                    <strong>
                      {option.title}
                    </strong>

                    <span>
                      {option.description}
                    </span>

                  </span>

                </label>

              )
            )}

          </div>


          {/* Bouton */}
          <button
            className="save-preferences-button"
            type="submit"
            disabled={saving}
          >

            {saving
              ? 'Enregistrement...'
              : 'Enregistrer mes préférences'
            }

          </button>

        </form>

      </div>

    </section>
  );
}