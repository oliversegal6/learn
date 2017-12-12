[TOC]
# NgxEditor.Md

 Angualr4 directive for markdown editor, wrapper by [edititor.md](https://github.com/pandao/editor.md) ，original project [demo](https://pandao.github.io/editor.md/)



# Installation

npm install --save ngx-editor.md@2.3.3
npm uninstall ngx-editor.md

# Usage

**html**

```html
<div id="ed" (onComplete)="onComplate($event)" appEditorMd [editorConfig]="conf"></div>
```

 Use the `appEditorMd` directive on the div element and must be set `id`  ,and then you can setting the editor config use `[editorConfig]` ,the directive will emmit the editor instance by `(onComplete)` when editor create complete。

**TypeScript**

**app.module.ts**

```typescript
import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {EditorMdModule} from 'ngx-editor.md';

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    EditorMdModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}

```

**component.ts**

```typescript
import {Component} from '@angular/core';
import {EditorConfig} from 'ngx-editor.md';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';
  conf = new EditorConfig();
  editorInstance: any;

  onComplate(editorInstance: any) {
    this.editorInstance = editorInstance;
  }
}

```

## EditorConfig

**default settings**

```typescript
export class EditorConfig {
  public width = '100%';
  public height = '400';
  public path = 'assets/lib/';
  public codeFold: true;
  public searchReplace = true;
  public toolbar = true;
  public emoji = true;
  public taskList = true;
  public tex = true;
  public readOnly = false;
  public tocm = true;
  public watch = true;
  public previewCodeHighlight = true;
  public saveHTMLToTextarea = true;
  public markdown = '';
  public flowChart = true;
  public syncScrolling = true;
  public sequenceDiagram = true;
  public imageUpload = true;
  public imageFormats = ['jpg', 'jpeg', 'gif', 'png', 'bmp', 'webp'];
  public imageUploadURL = '';

  constructor() {
  }
}
```

# Screenshot

![screentshot](demo.png)

# APIS

Please go to the original project , [click me](https://pandao.github.io/editor.md/examples/index.html)

