/** Generic envelope returned by every backend endpoint (see com.retailbus.dto.ApiResponse). */
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: number;
}

