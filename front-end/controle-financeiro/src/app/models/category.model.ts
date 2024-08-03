export interface Category {
  id: number;
  name: string;
  description?: string;
  valueExpected?: number;
  valueRealized?: number;
  isActive: boolean;
  creationDate: Date;
  updateDate: Date;
}
