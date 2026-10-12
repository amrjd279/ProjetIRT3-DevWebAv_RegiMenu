import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

export const getRandomMeals = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL}/meals/random`);
    return response.data.meals;
  } catch (error) {
    console.warn("Mode secours (Mock) activé : utilisation des images officielles Spoonacular.");
    // Données mockées avec les vraies URLs d'images officielles issues de ton JSON
    return [
      {
        id: 633166,
        title: "Soupe à l'avocat, au maïs, à la tomate et aux tortillas",
        imageUrl: "https://img.spoonacular.com/recipes/633166-556x370.jpg",
        readyInMinutes: 45,
        isVegetarian: false,
        isVegan: false
      },
      {
        id: 655103,
        title: "Crostata aux pêches",
        imageUrl: "https://img.spoonacular.com/recipes/655103-556x370.png",
        readyInMinutes: 45,
        isVegetarian: true,
        isVegan: false
      },
      {
        id: 716426,
        title: "Riz sauté au chou-fleur, au riz complet et aux légumes",
        imageUrl: "https://img.spoonacular.com/recipes/716426-556x370.jpg",
        readyInMinutes: 30,
        isVegetarian: true,
        isVegan: true
      }
    ];
  }
};

/**
 * Récupère les détails complets d'une recette par son identifiant.
 * @param {number|string} id - L'ID Spoonacular de la recette
 * @returns {Promise<Object>} Les détails de la recette (ingrédients, instructions, etc.)
 */
export const getMealDetails = async (id) => {
  const response = await axios.get(`${API_BASE_URL}/meals/${id}`);
  return response.data;
};