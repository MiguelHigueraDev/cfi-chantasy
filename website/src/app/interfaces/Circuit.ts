import { Country } from "./Country";

export interface Circuit {
    id: number;
    name: string;
    country: Country;
    isDeleted: string;
}