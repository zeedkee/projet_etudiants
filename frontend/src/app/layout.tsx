import type { Metadata } from 'next';
import './globals.css';
import Link from 'next/link';

export const metadata: Metadata = {
  title: 'Gestion des Étudiants',
  description: 'Frontend Next.js pour la gestion des étudiants',
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="fr">
      <body className="bg-gray-50 min-h-screen">
        <nav className="bg-indigo-600 text-white shadow-lg">
          <div className="max-w-7xl mx-auto px-4 py-4 flex items-center justify-between">
            <Link href="/" className="text-xl font-bold tracking-tight">
              ◆ Gestion Étudiants
            </Link>
            <div className="flex gap-6">
              <Link href="/etudiants" className="hover:text-indigo-200 transition">
                Étudiants
              </Link>
              <Link href="/departements" className="hover:text-indigo-200 transition">
                Départements
              </Link>
            </div>
          </div>
        </nav>
        <main className="max-w-7xl mx-auto px-4 py-8">{children}</main>
      </body>
    </html>
  );
}