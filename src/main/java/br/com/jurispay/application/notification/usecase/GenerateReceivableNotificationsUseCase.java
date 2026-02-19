package br.com.jurispay.application.notification.usecase;

public interface GenerateReceivableNotificationsUseCase {

    int generateReminders();

    int generateCollections();
}
