import React from 'react';
import { Link } from 'react-router-dom';
import PasswordList from '../components/PasswordList';

export default function Dashboard() {
  return (
    <>
      <h2>Your Passwords</h2>
      <Link to="/add-password">Add New</Link>
      <PasswordList />
    </>
  );
}
