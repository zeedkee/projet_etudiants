'use client';

import { useEffect, useState } from 'react';
import Link from 'next/link';
import api, { Etudiant } from '@/lib/api';

export default function EtudiantsPage() {
  const [etudiants, setEtudiants] = useState<Etudiant[]>([]);
  const [loading, setLoading] = useState(true);

  const loadEtudiants = async () => {
    setLoading(true);
    try {
      const res = await api.get<Etudiant[]>('/etudiants');
      setEtudiants(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { loadEtudiants(); }, []);

  const deleteEtudiant = async (id: number) => {
    if (!confirm('Supprimer cet étudiant ?')) return;
    try {
      await api.delete(`/etudiants/${id}`);
      loadEtudiants();
    } catch (err) {
      console.error(err);
    }
  };

  if (loading) return <p className="text-center py-10">Chargement...</p>;

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-gray-800">Étudiants</h1>
        <Link href="/etudiants/new"
          className="bg-indigo-600 text-white px-5 py-2 rounded-lg font-semibold hover:bg-indigo-700 transition">
          + Nouvel Étudiant
        </Link>
      </div>

      <div className="bg-white rounded-xl shadow overflow-hidden" data-testid="etudiant-list">
        <table className="w-full">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase">CIN</th>
              <th className="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase">Nom</th>
              <th className="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase">Date Naissance</th>
              <th className="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase">Âge</th>
              <th className="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase">Département</th>
              <th className="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase">Actions</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {etudiants.map((e) => (
              <tr key={e.id} className="hover:bg-gray-50" data-testid="etudiant-item">
                <td className="px-6 py-4 font-mono text-sm text-indigo-600">{e.cin}</td>
                <td className="px-6 py-4 font-semibold">{e.nom}</td>
                <td className="px-6 py-4 text-gray-500">{e.dateNaissance}</td>
                <td className="px-6 py-4">{e.age} ans</td>
                <td className="px-6 py-4">
                  {e.departementNom && (
                    <span className="bg-green-100 text-green-700 px-3 py-1 rounded-full text-xs font-semibold">
                      {e.departementNom}
                    </span>
                  )}
                </td>
                <td className="px-6 py-4 flex gap-2">
                  <Link href={`/etudiants/${e.id}`}
                    className="text-indigo-600 hover:text-indigo-800 text-sm font-medium">
                    Modifier
                  </Link>
                  <button onClick={() => deleteEtudiant(e.id)}
                    data-testid="delete-btn"
                    className="text-red-500 hover:text-red-700 text-sm font-medium">
                    Supprimer
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}