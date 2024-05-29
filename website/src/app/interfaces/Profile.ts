import { Role } from "./Role";

/** User profile */
export interface Profile {
    id: number;
    username: string;
    displayName: string;
    role: Role;
    enabled: boolean;
    authorities: string[];
    credentialsNonExpired: boolean;
    accountNonExpired: boolean;
    accountNonLocked: boolean;
}