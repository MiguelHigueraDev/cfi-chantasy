import { Country } from "./Country";
import { Team } from "./Team";

export interface Driver {
    id: number;
    name: string;
    code: string;
    country: Country;
    team: Team;
    isDeleted: boolean;
}