import { Question } from "./Question";
import { User } from "./User";

export interface Answer {
    id: number;
    userId: number;
    user: User;
    answer: string;
    question: Question;
    isCorrect: boolean;
}