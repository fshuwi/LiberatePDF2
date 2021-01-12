import { BrowserModule } from '@angular/platform-browser';
import { NgModule, APP_INITIALIZER } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { StatisticsComponent } from './statistics/statistics.component';
import { FileListComponent } from './file-list/file-list.component';

import { HttpClientModule } from '@angular/common/http';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { RestrictionRemoverService } from './restriction-remover.service';
import { SettingsHttpService } from './settings-http.service';

import {
  ApiModule,
  Configuration,
  ConfigurationParameters,
} from './restclient';

export function app_Init(settingsHttpService: SettingsHttpService) {
  return () => settingsHttpService.initializeApp();
}

export function apiConfigFactory(): Configuration {
  const params: ConfigurationParameters = {
    // set configuration parameters here.
    basePath: 'http://localhost:8080',
  };
  return new Configuration(params);
}

@NgModule({
  declarations: [AppComponent, StatisticsComponent, FileListComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    //NgbModule.forRoot(),
    NgbModule,
    ApiModule.forRoot(apiConfigFactory),
  ],
  providers: [
    RestrictionRemoverService,
    {
      provide: APP_INITIALIZER,
      useFactory: app_Init,
      deps: [SettingsHttpService],
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
