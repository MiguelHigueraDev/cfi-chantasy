import { Answer } from "./Answer";
import { Race } from "./Race";

export interface Question {
    id: number;
    race: Race;
    question: string;
    points: number;
    isDeleted: boolean;
    answers: Answer[];
}