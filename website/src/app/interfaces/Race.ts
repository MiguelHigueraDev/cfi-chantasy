import { Circuit } from "./Circuit";
import { Driver } from "./Driver";
import { FreePrediction } from "./FreePrediction";
import { Question } from "./Question";
import { Result } from "./Result";

export interface Race {
    id: number;
    name: string;
    circuit: Circuit;
    maxDnfAwarded: number;
    positionPredictionRangeStart: number;
    positionPredictionRangeEnd: number;
    dnfPoints: number;
    positionPoints: number;
    predictionStartDate: string;
    predictionEndDate: string;
    maxFreePredictions: number;
    isQualifier: boolean;
    isDeleted: boolean;
    questions: Question[];
    freePredictions: FreePrediction[];
    drivers: Driver[];
    results: Result[];
}