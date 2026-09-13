import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HistoryService, HistoryItemResponse } from '../../services/history';

@Component({
    selector: 'app-history',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './history.html',
    styleUrl: './history.css'
})
export class History implements OnInit {

    history: HistoryItemResponse[] = [];
    errorMessage = '';

    constructor(
        private historyService: HistoryService,
        private cdr: ChangeDetectorRef
    ) { }

    ngOnInit(): void {
        this.loadHistory();
    }

    loadHistory(): void {
        this.historyService.getHistory().subscribe({
            next: (response) => {
                this.history = response;
                this.cdr.detectChanges();
            },
            error: (error) => {
                console.error(error);
                this.errorMessage = 'Erro ao carregar histórico.';
                this.cdr.detectChanges();
            }
        });
    }
}