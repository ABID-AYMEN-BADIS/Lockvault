import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Navbar() {
  const { token, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav style={{ padding: '10px', background: '#333', color: '#fff' }}>
      <Link to="/" style={{ marginRight: 15 }}>SecureVault</Link>
      {token ? (
        <>
          <Link to="/dashboard" style={{ marginRight: 15 }}>Dashboard</Link>
          <button onClick={handleLogout}>Logout</button>
        </>
      ) : (
        <>          
          <Link to="/login" style={{ marginRight: 15 }}>Login</Link>
          <Link to="/register">Register</Link>
        </>
      )}
    </nav>
  );
}
