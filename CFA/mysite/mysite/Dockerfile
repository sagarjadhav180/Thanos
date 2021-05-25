FROM python:3.8.3-alpine
ENV PYTHONDONTWRITEBYTECODE 1 
ENV PYTHONUNBUFFERED 1
WORKDIR /usr/src/app
RUN pip3 install Django
RUN apk update
COPY ./requirements.txt /usr/src/app
RUN apk add --no-cache git
RUN pip3 install gitpython
RUN apk add --no-cache postgresql-libs &&  apk add --no-cache --virtual .build-deps gcc musl-dev postgresql-dev &&  python3 -m pip install -r requirements.txt --no-cache-dir &&  apk --purge del .build-deps
# COPY ./requirements.txt /usr/src/app
# RUN pip3 install -r ./requirements.txt
COPY . .
EXPOSE 8000
CMD ["python3", "manage.py", "runserver", "0.0.0.0:8000"]
 