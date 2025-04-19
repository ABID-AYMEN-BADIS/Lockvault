import React, { useEffect, useState } from 'react';
import PasswordItem from './PasswordItem';
import passwordService from '../services/passwordService';
import { useAuth } from '../context/AuthContext';

export default function PasswordList() {
  const { token } = useAuth();
  const [entries, setEntries] = useState([]);

  useEffect(() => {
    if (!token) {
      console.warn("Token is null or undefined!");
      return;
    }
    async function fetch() {
      console.log("Sending token:", token);
      const list = await passwordService.getAll( token);
      setEntries(list);
    }
    if (token) fetch();
  }, [token]);

  const handleDelete = async (id) => {
    await passwordService.delete(id, token);
    setEntries(entries.filter(e => e.id !== id));
  };

  return (
    <div>
      {entries.map(e => <PasswordItem key={e.id} entry={e} onDelete={handleDelete} />)}
    </div>
  );
}
