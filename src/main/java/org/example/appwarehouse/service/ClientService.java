package org.example.appwarehouse.service;

import org.example.appwarehouse.entity.Client;
import org.example.appwarehouse.payload.Result;
import org.example.appwarehouse.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Result createClient(Client client) {
        boolean existsByPhoneNumber = clientRepository.existsByPhoneNumber(client.getPhoneNumber());
        if(existsByPhoneNumber) {
            return new Result("Client already exists",false);
        }

        Client saved = clientRepository.save(client);
        if(saved == null) {
            return new Result("Client yaratilmadi",false);
        }

        return new Result("Client yaratildi",true);
    };

    public Result updateClient(Integer id,Client client) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if(!optionalClient.isPresent()) {
            return new Result("Bundey Client yo'q ",false);
        }

        if(!client.getPhoneNumber().equals(optionalClient.get().getPhoneNumber())) {
            boolean existsByPhoneNumber = clientRepository.existsByPhoneNumber(client.getPhoneNumber());
            if(existsByPhoneNumber) {
                return new Result("Client already exists",false);
            }
        }

        Client saved = clientRepository.save(client);
        if(saved == null) {
            return new Result("Client o'zgartirilmad",false);
        }
        return new Result("Client o'zgartirildi",true);
    }

    public List<Client> allClients() {
        return  clientRepository.findAll();
    }

    public Result deleteClient(Integer id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if(!optionalClient.isPresent()) {
            return new Result("Bundey Client yo'q ",false);
        }
        Client client = optionalClient.get();
        clientRepository.delete(client);
        return new Result("Client o'chirildi",true);
    }


}
