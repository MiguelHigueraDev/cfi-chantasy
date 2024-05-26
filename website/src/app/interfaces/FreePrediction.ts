/**
 * Free predictions entered by users
 */

import { Race } from "./Race";

export interface FreePrediction {
  id: number;
  race: Race;
  prediction: string;
  userId: number;
}