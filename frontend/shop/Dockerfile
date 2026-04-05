# Node.js 22.12.0 이미지 사용
FROM node:22.12.0

# 작업 디렉토리 설정
WORKDIR /app

# package.json과 package-lock.json 복사
COPY package.json package-lock.json ./

# 의존성 설치
RUN npm install

# 소스 코드 복사 (필요한 폴더만 선택적으로 복사)
COPY public ./public
COPY src ./src
COPY vite.config.js ./
COPY index.html ./

# React 애플리케이션 빌드
RUN npm run build

# 애플리케이션 실행
CMD ["npm", "run", "dev"]

# 포트 노출
EXPOSE 3000
