import axios from 'axios';

const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL ||
  'http://localhost:8081/api';

/**
 * =========================================================
 * MENUS
 * =========================================================
 */

/**
 * Récupère le menu du jour.
 */
export const getTodayMeals = async (userId = null) => {
  try {
    const params = userId ? { userId } : {};

    const response = await axios.get(
      `${API_BASE_URL}/meals/today`,
      { params }
    );

    return response.data.meals;

  } catch (error) {

    console.warn(
      '⚠️ getTodayMeals: bascule sur getRandomMeals en secours',
      error
    );

    return getRandomMeals();
  }
};

/**
 * Régénère les repas du jour.
 */
export const regenerateMeals = async (
  userId = null
) => {

  const params = userId
    ? { userId }
    : {};

  const response = await axios.post(
    `${API_BASE_URL}/meals/regenerate`,
    null,
    { params }
  );

  return response.data.meals;
};

/**
 * =========================================================
 * RECETTES
 * =========================================================
 */

/**
 * Récupère des recettes aléatoires.
 */
export const getRandomMeals = async () => {

  try {

    const response = await axios.get(
      `${API_BASE_URL}/meals/random`
    );

    return response.data.meals;

  } catch (error) {

    console.warn(
      '⚠️ Mode secours (Mock) activé pour l’accueil.'
    );

    return [

      {
        id: 633166,
        title:
          'Soupe à l’avocat, au maïs, à la tomate et aux tortillas',
        imageUrl:
          'https://img.spoonacular.com/recipes/633166-556x370.jpg',
        readyInMinutes: 45,
        isVegetarian: false,
        isVegan: false
      },

      {
        id: 655103,
        title: 'Crostata aux pêches',
        imageUrl:
          'https://img.spoonacular.com/recipes/655103-556x370.png',
        readyInMinutes: 45,
        isVegetarian: true,
        isVegan: false
      },

      {
        id: 716426,
        title:
          'Riz sauté au chou-fleur, au riz complet et aux légumes',
        imageUrl:
          'https://img.spoonacular.com/recipes/716426-556x370.jpg',
        readyInMinutes: 30,
        isVegetarian: true,
        isVegan: true
      }

    ];
  }
};

/**
 * Récupère les détails d'une recette.
 */
export const getRecipeDetails = async (id) => {

  try {

    const response = await axios.get(
      `${API_BASE_URL}/meals/${id}`
    );

    return response.data;

  } catch (error) {

    console.warn(
      `⚠️ Mode secours (Mock) activé pour les détails de l’ID ${id}`
    );

    const mockRecipes = {

      633166: {

        id: 633166,

        title:
          'Soupe à l’avocat, au maïs, à la tomate et aux tortillas',

        imageUrl:
          'https://img.spoonacular.com/recipes/633166-556x370.jpg',

        readyInMinutes: 45,

        servings: 4,

        isVegetarian: false,

        isVegan: false,

        extendedIngredients: [
          {
            id: 1,
            original: '2 avocats mûrs, coupés en dés'
          },
          {
            id: 2,
            original: '200g de maïs doux'
          },
          {
            id: 3,
            original: '3 tomates fraîches coupées en dés'
          },
          {
            id: 4,
            original: '1 litre de bouillon de légumes'
          },
          {
            id: 5,
            original:
              'Chips de tortilla croustillantes'
          }
        ],

        instructions:
          '1. Dans une grande casserole, porter le bouillon de légumes à ébullition.\n' +
          '2. Ajouter les tomates et le maïs, puis laisser mijoter 15 minutes.\n' +
          '3. Incorporer délicatement les dés d’avocat juste avant de servir.\n' +
          '4. Servir chaud dans des bols avec des chips de tortilla.'
      },

      655103: {

        id: 655103,

        title: 'Crostata aux pêches',

        imageUrl:
          'https://img.spoonacular.com/recipes/655103-556x370.png',

        readyInMinutes: 45,

        servings: 6,

        isVegetarian: true,

        isVegan: false,

        extendedIngredients: [
          {
            id: 1,
            original: '1 pâte brisée sablée'
          },
          {
            id: 2,
            original:
              '4 pêches mûres tranchées'
          },
          {
            id: 3,
            original:
              '50g de sucre de canne'
          },
          {
            id: 4,
            original:
              '1 cuillère à soupe de jus de citron'
          }
        ],

        instructions:
          '1. Préchauffer le four à 180°C.\n' +
          '2. Étaler la pâte sur une plaque de cuisson.\n' +
          '3. Disposer les tranches de pêches au centre en laissant les bords libres.\n' +
          '4. Saupoudrer de sucre, replier les bords et enfourner 35 minutes.'
      },

      716426: {

        id: 716426,

        title:
          'Riz sauté au chou-fleur, au riz complet et aux légumes',

        imageUrl:
          'https://img.spoonacular.com/recipes/716426-556x370.jpg',

        readyInMinutes: 30,

        servings: 4,

        isVegetarian: true,

        isVegan: true,

        extendedIngredients: [
          {
            id: 1,
            original:
              '2 tasses de riz complet cuit'
          },
          {
            id: 2,
            original:
              '1 tête de chou-fleur râpée'
          },
          {
            id: 3,
            original:
              '1 tasse de petits pois et carottes'
          },
          {
            id: 4,
            original:
              '2 cuillères à soupe de sauce soja'
          }
        ],

        instructions:
          '1. Faire sauter le chou-fleur et les légumes dans une poêle chaude avec un filet d’huile.\n' +
          '2. Ajouter le riz complet cuit.\n' +
          '3. Verser la sauce soja, bien mélanger et servir chaud.'
      }
    };

    return (
      mockRecipes[id] ||
      mockRecipes[633166]
    );
  }
};

/**
 * Alias conservé pour compatibilité.
 */
export const getMealDetails =
  getRecipeDetails;


/**
 * =========================================================
 * PROFIL
 * =========================================================
 */

/**
 * Ancienne méthode :
 *
 * GET /api/profile/{id}
 */
export const getUserProfileById =
  async (userId) => {

    const response = await axios.get(
      `${API_BASE_URL}/profile/${userId}`
    );

    return response.data;
  };


/**
 * Ancienne méthode conservée.
 *
 * Met à jour les préférences avec les anciens endpoints.
 */
export const updateUserPreferences =
  async (
    preferences,
    userId = null
  ) => {

    const url = userId
      ? `${API_BASE_URL}/profile/${userId}/preferences`
      : `${API_BASE_URL}/profile/preferences`;

    const response = await axios.put(
      url,
      preferences
    );

    return response.data;
  };


/**
 * =========================================================
 * US4.2
 * =========================================================
 */

/**
 * GET /api/profile
 *
 * Récupère le profil courant.
 */
export const getUserProfile =
  async () => {

    const response = await axios.get(
      `${API_BASE_URL}/profile`
    );

    return response.data;
  };


/**
 * PUT /api/profile
 *
 * Enregistre les préférences alimentaires.
 */
export const updateUserProfile =
  async (preferences) => {

    const response = await axios.put(
      `${API_BASE_URL}/profile`,
      preferences
    );

    return response.data;
  };