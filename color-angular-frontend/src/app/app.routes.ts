import { Routes } from '@angular/router';
import { DummySiteOne } from '../dummy-sites/dummy-site-one';
import { DummySiteThree } from '../dummy-sites/dummy-site-three';
import { DummySiteTwo } from '../dummy-sites/dummy-site-two';

export const routes: Routes = [
  {
    path: 'one',
    component: DummySiteOne,
  },
  {
    path: 'two',
    component: DummySiteTwo,
  },
  {
    path: 'three',
    component: DummySiteThree,
  },
];
