'use client';

import { useEffect, useState } from 'react';
import { useRouter, useParams } from 'next/navigation';
import api, { Departement } from '@/lib/api';

export default function EditEtudiantPage() {
  const router = useRouter();
  const params = useParams();
  const id = params.id;
  const [departements, setDepartements] = useState<Departement[]>([]);
  const [form, setForm] = useState({
    cin: '', nom: '', dateNaissance: '', email: '',
    anneePremiereInscription: 2024, departementId: '',
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      api.get(`/etudiants/${id}`),
      api.get<Departement[]>('/departements'),
    ]).then(([etRes, deptRes]) => {
      const e = etRes.data;
      setForm({
        cin: e.cin, nom: e.nom, dateNaissance: e.dateNaissance, email: e.email || '',
        anneePremiereInscription: e.anneePremiereInscription,
        departementId: e.departementId ? e.departementId.toString() : '',
      });
      setDepartements(deptRes.data);
      setLoading(false);
    });
  }, [id]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.put(`/etudiants/${id}`, {
        ...form,
        departementId: form.departementId ? parseInt(form.departementId) : null,
      });
      router.push('/etudiants');
    } catch (err) {
      console.error(err);
      alert('Erreur lors de la modification');
    }
  };

  if (loading) return <p className="text-center py-10">Chargement...</p>;

  return (
    <div className="max-w-lg mx-auto">
      <h1 className="text-2xl font-bold text-gray-800 mb-6">Modifier l'Étudiant</h1>
      <form onSubmit={handleSubmit} className="bg-white rounded-xl shadow p-6 space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-600 mb-1">CIN</label>
          <input type="text" required value={form.cin}
            onChange={(e) => setForm({ ...form, cin: e.target.value })}
            className="w-full border rounded-lg px-4 py-2 focus:ring-2 focus:ring-indigo-500 outline-none" />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-600 mb-1">Nom</label>
          <input type="text" required value={form.nom}
            onChange={(e) => setForm({ ...form, nom: e.target.value })}
            className="w-full border rounded-lg px-4 py-2 focus:ring-2 focus:ring-indigo-500 outline-none" />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-600 mb-1">Date de Naissance</label>
          <input type="date" required value={form.dateNaissance}
            onChange={(e) => setForm({ ...form, dateNaissance: e.target.value })}
            className="w-full border rounded-lg px-4 py-2 focus:ring-2 focus:ring-indigo-500 outline-none" />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-600 mb-1">Email</label>
          <input type="email" value={form.email}
            onChange={(e) => setForm({ ...form, email: e.target.value })}
            className="w-full border rounded-lg px-4 py-2 focus:ring-2 focus:ring-indigo-500 outline-none" />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-600 mb-1">Année 1ère Inscription</label>
          <input type="number" value={form.anneePremiereInscription}
            onChange={(e) => setForm({ ...form, anneePremiereInscription: parseInt(e.target.value) })}
            className="w-full border rounded-lg px-4 py-2 focus:ring-2 focus:ring-indigo-500 outline-none" />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-600 mb-1">Département</label>
          <select value={form.departementId}
            onChange={(e) => setForm({ ...form, departementId: e.target.value })}
            className="w-full border rounded-lg px-4 py-2 focus:ring-2 focus:ring-indigo-500 outline-none">
            <option value="">-- Choisir --</option>
            {departements.map((d) => (
              <option key={d.id} value={d.id}>{d.nom}</option>
            ))}
          </select>
        </div>
        <div className="flex gap-3 pt-2">
          <button type="submit"
            className="bg-indigo-600 text-white px-6 py-2 rounded-lg font-semibold hover:bg-indigo-700">
            Enregistrer
          </button>
          <button type="button" onClick={() => router.push('/etudiants')}
            className="border px-6 py-2 rounded-lg text-gray-600 hover:bg-gray-50">
            Annuler
          </button>
        </div>
      </form>
    </div>
  );
}