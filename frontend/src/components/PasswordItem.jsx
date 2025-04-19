import React from 'react';

export default function PasswordItem({ entry, onDelete }) {
  return (
    <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 10, padding: 10, background: '#fff', borderRadius: 4 }}>
      <div>
        <strong>{entry.platform}</strong> — {entry.username} — {entry.password}
      </div>
      <button onClick={() => onDelete(entry.id)}>Delete</button>
    </div>
  );
}