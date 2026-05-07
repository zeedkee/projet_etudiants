import axios from 'axios';

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api',
});

export interface Etudiant {
  id: number;
  cin: string;
  nom: string;
  dateNaissance: string;
  email: string;
  anneePremiereInscription: number;
  age: number;
  departementId: number | null;
  departementNom: string | null;
}

export interface Departement {
  id: number;
  nom: string;
}

export interface Note {
  id: number;
  studentId: number;
  matiere: string;
  valeur: number;
}

export default api;