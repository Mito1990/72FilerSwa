# pull official base image
FROM node:20
WORKDIR /Frontend
COPY package.json .
COPY package-lock.json .
RUN npm install --silent
RUN npm install react-scripts --silent
RUN npx update-browserslist-db@latest
COPY . .
# install app dependencies
# start app
EXPOSE 3000
CMD ["npm", "start"]
