import { provideZonelessChangeDetection } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { DummySiteOne } from '../dummy-site-one';
import { DummySiteTwo } from '../dummy-site-two';
import { DummySiteThree } from '../dummy-site-three';

for (const aDummySite of [DummySiteOne, DummySiteTwo, DummySiteThree]) {
  describe(aDummySite.name, () => {
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        imports: [aDummySite],
        providers: [provideZonelessChangeDetection()],
      }).compileComponents();
    });

    it('should create the app', () => {
      const fixture = TestBed.createComponent(aDummySite);
      const dummyInstance = fixture.componentInstance;
      expect(dummyInstance).toBeTruthy();
    });

    it('should render title', () => {
      const fixture = TestBed.createComponent(aDummySite);
      fixture.detectChanges();
      const compiled = fixture.nativeElement as HTMLElement;
      expect(compiled.querySelector('p')?.textContent).toContain('Hello From Dummy Site');
    });
  });
}
