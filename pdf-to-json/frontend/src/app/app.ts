import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConverterService } from './converter.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  title = 'pdf-to-json-ui';
  selectedFile: File | null = null;
  loading = false;
  result: any = null;
  error: string | null = null;

  constructor(private converterService: ConverterService) {}

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  onUpload() {
    if (!this.selectedFile) return;

    this.loading = true;
    this.result = null;
    this.error = null;

    this.converterService.upload(this.selectedFile).subscribe({
      next: (res) => {
        this.result = res;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to convert PDF. Please check backend and API key.';
        console.error(err);
        this.loading = false;
      }
    });
  }

  downloadJson() {
    if (!this.result) return;
    const blob = new Blob([JSON.stringify(this.result, null, 2)], { type: 'application/json' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'converted.json';
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
