export interface PageParams {
    page: number;
    size: number;
}
export interface Student {
    studentId: number;
    name: string;
    studentNumber: string;
    grade: string;
    studentClass: string;
    permiss: string[]
}