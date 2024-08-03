export interface Transaction {
  id: number;
  transactionType: string;
  transactionValue: number;
  transactionDate: Date;
  updateDate: Date;
  categoryId: number;
  description?: string;
  userId: number;
  paymentMethod?: string;
}
