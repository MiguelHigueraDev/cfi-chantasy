import { Question } from "./Question";

export interface Answer {
    id: number;
    userId: number;
    user: User;
    answer: string;
    question: Question;
    isCorrect: boolean;
}