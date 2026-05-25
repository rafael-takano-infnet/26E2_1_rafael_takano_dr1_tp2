package com.example.passagem.services;

import com.example.passagem.dtos.PassagemRequestDTO;
import com.example.passagem.dtos.PassagemResponseDTO;
import com.example.passagem.models.Passagem;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PassagemService {
    private List<Passagem> passagens = new ArrayList<>();
    private Long idCounter = 1L;

    public PassagemService() {
        Passagem p1 = new Passagem(idCounter++, "Joao Silva", 1, "Sao Paulo", "Rio de Janeiro", LocalDate.of(2026, 6, 15), "CONFIRMADA");
        Passagem p2 = new Passagem(idCounter++, "Maria Santos", 2, "Curitiba", "Florianopolis", LocalDate.of(2026, 6, 20), "CONFIRMADA");
        Passagem p3 = new Passagem(idCounter++, "Carlos Oliveira", 3, "Belo Horizonte", "Vitoria", LocalDate.of(2026, 7, 10), "PENDENTE");
        passagens.add(p1);
        passagens.add(p2);
        passagens.add(p3);
    }

    private Passagem toEntity(PassagemRequestDTO dto) {
        return new Passagem(null, dto.getPassageiro(), dto.getAssento(), dto.getOrigem(), dto.getDestino(), dto.getData(), dto.getStatus());
    }

    private PassagemResponseDTO toResponseDTO(Passagem passagem) {
        return new PassagemResponseDTO(passagem.getId(), passagem.getPassageiro(), passagem.getAssento(), passagem.getOrigem(), passagem.getDestino(), passagem.getData(), passagem.getStatus());
    }

    public List<PassagemResponseDTO> findAll() {
        return passagens.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public PassagemResponseDTO findById(Long id) {
        Passagem passagem = passagens.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Passagem nao encontrada"));
        return toResponseDTO(passagem);
    }

    public PassagemResponseDTO create(PassagemRequestDTO dto) {
        boolean assentoExistente = passagens.stream().anyMatch(p -> p.getAssento().equals(dto.getAssento()));
        if (assentoExistente) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assento ja esta em uso");
        }
        Passagem passagem = toEntity(dto);
        passagem.setId(idCounter++);
        passagens.add(passagem);
        return toResponseDTO(passagem);
    }

    public PassagemResponseDTO update(Long id, PassagemRequestDTO dto) {
        Passagem passagem = passagens.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Passagem nao encontrada"));
        passagem.setPassageiro(dto.getPassageiro());
        passagem.setAssento(dto.getAssento());
        passagem.setOrigem(dto.getOrigem());
        passagem.setDestino(dto.getDestino());
        passagem.setData(dto.getData());
        passagem.setStatus(dto.getStatus());
        return toResponseDTO(passagem);
    }

    public void delete(Long id) {
        Passagem passagem = passagens.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Passagem nao encontrada"));
        passagens.removeIf(p -> p.getId().equals(id));
    }

    public List<PassagemResponseDTO> findByDestino(String destino) {
        return passagens.stream()
                .filter(p -> p.getDestino().equalsIgnoreCase(destino))
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
