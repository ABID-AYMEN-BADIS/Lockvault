import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth';

export default {
  register: async ({ email, password, firstName, lastName }) => {
    await axios.post(`${API_URL}/register`, { email, password, firstName, lastName });
  },

  login: async ({ email, password }) => {
    const response = await axios.post(`${API_URL}/login`, { email, password });
    return response.data; // JWT token
  },
};
