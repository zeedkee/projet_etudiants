'use client';

import { useEffect, useState } from 'react';
import api, { Departement } from '@/lib/api';

export default function DepartementsPage() {
  const [departements, setDepartements] = useState<Departement[]>([]);
  const [nom, setNom] = useState('');
  const [editId, setEditId] = useState<number | null>(null);
  const [editNom, setEditNom] = useState('');

  const load = async () => {
    const res = await api.get<Departement[]>('/departements');
    setDepartements(res.data);
  };

  useEffect(() => { load(); }, []);

  const create = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!nom.trim()) return;
    await api.post('/departements', { nom });
    setNom('');
    load();
  };

  const startEdit = (d: Departement) => {
    setEditId(d.id);
    setEditNom(d.nom);
  };

  const saveEdit = async () => {
    if (!editNom.trim() || !editId) return;
    await api.put(`/departements/${editId}`, { nom: editNom });
    setEditId(null);
    load();
  };

  const remove = async (id: number) => {
    if (!confirm('Supprimer ce département ?')) return;
    await api.delete(`/departements/${id}`);
    load();
  };

  return (
    <div className="max-w-2xl mx-auto">
      <h1 className="text-2xl font-bold text-gray-800 mb-6">Départements</h1>

      {/* Create form */}
      <form onSubmit={create} className="flex gap-3 mb-6">
        <input type="text" value={nom} onChange={(e) => setNom(e.target.value)}
          placeholder="Nom du département"
          className="flex-1 border rounded-lg px-4 py-2 focus:ring-2 focus:ring-indigo-500 outline-none" />
        <button type="submit"
          className="bg-indigo-600 text-white px-6 py-2 rounded-lg font-semibold hover:bg-indigo-700">
          Ajouter
        </button>
      </form>

      {/* List */}
      <div className="bg-white rounded-xl shadow divide-y">
        {departements.map((d) => (
          <div key={d.id} className="flex items-center justify-between px-6 py-4">
            {editId === d.id ? (
              <div className="flex gap-2 flex-1">
                <input type="text" value={editNom} onChange={(e) => setEditNom(e.target.value)}
                  className="flex-1 border rounded-lg px-3 py-1 outline-none focus:ring-2 focus:ring-indigo-500" />
                <button onClick={saveEdit} className="text-green-600 font-semibold text-sm">Sauver</button>
                <button onClick={() => setEditId(null)} className="text-gray-400 text-sm">Annuler</button>
              </div>
            ) : (
              <>
                <span className="font-semibold">{d.nom}</span>
                <div className="flex gap-3">
                  <button onClick={() => startEdit(d)} className="text-indigo-600 text-sm font-medium">Modifier</button>
                  <button onClick={() => remove(d.id)} className="text-red-500 text-sm font-medium">Supprimer</button>
                </div>
              </>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}