import Link from 'next/link';

export default function Home() {
  return (
    <div className="text-center py-20">
      <h1 className="text-4xl font-bold text-gray-800 mb-4">Gestion des Étudiants</h1>
      <p className="text-gray-500 mb-10">Application de gestion avec Next.js + Spring Boot</p>
      <div className="flex justify-center gap-6">
        <Link href="/etudiants"
          className="bg-indigo-600 text-white px-8 py-3 rounded-lg font-semibold hover:bg-indigo-700 transition">
          Gérer les Étudiants
        </Link>
        <Link href="/departements"
          className="bg-white text-indigo-600 border-2 border-indigo-600 px-8 py-3 rounded-lg font-semibold hover:bg-indigo-50 transition">
          Gérer les Départements
        </Link>
      </div>
    </div>
  );
}