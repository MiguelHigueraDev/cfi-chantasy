import { Driver } from "./Driver";

export interface Result {
    id: number;
    driver: Driver;
    position: number;
    didFinish: boolean;
}