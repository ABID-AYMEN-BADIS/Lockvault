import { useState } from 'react';

export default function PasswordItem({ entry, onDelete }) {
  const [show, setShow] = useState(false);

  return (
    <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 10, padding: 10, background: '#fff', borderRadius: 4 }}>
      <div>
        <strong>{entry.platform}</strong> — {entry.username} — 
        {show ? entry.password : '•'.repeat(10)}
      </div>
      <div>
        <button onClick={() => setShow(prev => !prev)} style={{ marginRight: 10 }}>
          {show ? 'Hide' : 'Show'}
        </button>
        <button onClick={() => onDelete(entry.id)}>Delete</button>
      </div>
    </div>
  );
}
