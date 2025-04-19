import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import passwordService from '../services/passwordService';

export default function AddPassword() {
  const { token } = useAuth();
  
  const navigate = useNavigate();
  const [form, setForm] = useState({ platform: '', username: '', password: '' });
  const [error, setError] = useState('');

  const handleChange = e => setForm({ ...form, [e.target.name]: e.target.value });
  const handleSubmit = async e => {
    e.preventDefault();
    try {
      await passwordService.add(form , token);
      navigate('/dashboard');
    } catch (err) {
      setError(err.response?.data || err.message);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Add Password</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <input name="platform" placeholder="Platform" value={form.platform} onChange={handleChange} />
      <input name="username" placeholder="Username" value={form.username} onChange={handleChange} />
      <input name="password" type="password" placeholder="Password" value={form.password} onChange={handleChange} />
      <button type="submit">Add</button>
    </form>
  );
}
