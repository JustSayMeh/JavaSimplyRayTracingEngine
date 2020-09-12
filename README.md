# Java Simply Raytracing App
Приложения для рендеринга сцен с использованием технологии трассировки лучей.
## Возможности
Приложение способно отоброжать примитивы:
  - Шар                                     [Тип Sphere]
  - Треугольник                             [Тип Triangle]
  - Плоскость                               [Тип Plane]
  - Тетраэдр                                [Тип Tetrahedron]
  - Произвольная выпусклый многоугольник    [Тип Polygon]

Фигуры задаются в файле конфигурации
```
[Sphere]
    radius: 10
    position: -33 -2  0
    name: spereone
    material: yellowM
[Plane]
    up: 0 1 0
    distance: -15
    name: mirror
    material: mirror
[Sphere]
    radius: 3
    position: -10 -2  -15
    name: glasssphere
    material: glassclear

[Tetrahedron]
    position: -30 -30 25 35 -30 25 0 35 25 -35 -30 55
    name: glasstetrahedron
    material: glassclear
```
Материалы задаются в файле конфигурации другого типа
```
[redM]
    color: 0.3 0.1 0.1
    specular: 0.6
    diffusion: 0.5
    mirror coefficient: 10

[mirror]
    color: 1 1 1
    specular: 10
    diffusion: 0.1
    mirror coefficient: 1425
    mirror ratio: 0.9

[glass]
    color: 1 1 1
    specular: 0.5
    diffusion: 0.1
    mirror coefficient: 125
    mirror ratio: 0.1
    refractive index: 1.5
    refractive albedo: 0.8

[glassclear]
    color: 1 1 1
    specular: 0.5
    diffusion: 0.1
    mirror coefficient: 125
    mirror ratio: 0.1
    refractive index: 1
    refractive albedo: 0.8

[yellowM]
    color: 0.4 0.4 0.3
    specular: 0.1
    diffusion: 0.3
    mirror coefficient: 10
```

Помимо вышенаписанного можно менять положение камеры и координаты источников света.
## Скриншоты
![alt text](https://github.com/JustSayMeh/JavaSimplyRayTracingEngine/blob/master/images/1.png?raw=true)
![alt text](https://github.com/JustSayMeh/JavaSimplyRayTracingEngine/blob/master/images/2.png?raw=true)
![alt text](https://github.com/JustSayMeh/JavaSimplyRayTracingEngine/blob/master/images/3.png?raw=true)