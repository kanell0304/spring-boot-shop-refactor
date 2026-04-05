import { Cookies } from "react-cookie";

const cookies = new Cookies();

/** 쿠키 저장 */
export const setCookie = (name, value, days = 1) => {
  const expires = new Date();
  expires.setDate(expires.getDate() + days);

  const stringValue = typeof value === "object" ? JSON.stringify(value) : value;
  cookies.set(name, stringValue, { path: "/", expires });
};

/** 쿠키 가져오기 (자동 파싱 시도) */
export const getCookie = (name) => {
  const rawValue = cookies.get(name);
  if (!rawValue) return null;

  try {
    return JSON.parse(rawValue); // JSON 문자열일 경우 파싱
  } catch (e) {
    return rawValue; // 일반 문자열일 경우 그대로 반환
  }
};

/** 쿠키 삭제 */
export const removeCookie = (name, path = "/") => {
  cookies.remove(name, { path });
};
