import axios from 'axios';
import { useAuth } from '../context/AuthContext';

// Helper to include JWT in headers
function authHeader(token) {
  return { headers: { Authorization: `Bearer ${token}` } };
}

export default {
  getAll: async (token) => {
    const response = await axios.get(`/api/passwords/me`, authHeader(token));
    return response.data;
  },
  add: async ({  platform, username, password }, token) => {
    await axios.post(
      `/api/passwords/add?platform`, 
      {platform , username , password},
      authHeader(token)
    );
  },
  delete: async (id, token) => {
    await axios.delete(`/api/passwords/delete/${id}`, authHeader(token));
  },
};