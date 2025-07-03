package br.com.fiquepositivo.api.mapper;

import br.com.fiquepositivo.api.dto.input.GastoRequest;
import br.com.fiquepositivo.api.dto.output.GastoDTO;
import br.com.fiquepositivo.domain.model.FormaPagamento;
import br.com.fiquepositivo.domain.model.Gasto;
import br.com.fiquepositivo.domain.model.Pessoa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class GastoMapperTest {

    @InjectMocks
    GastoMapper gastoMapper;

    @DisplayName("Deve converter GastoRequest em Gasto")
    @Test
    void testToEntity() {
        GastoRequest gastoRequest =
                new GastoRequest(1550.0, FormaPagamento.DINHEIRO, LocalDate.of(2024, 3, 22),
                        "Brasília", "Geladeira", true, 1);
        Gasto gasto = gastoMapper.toEntity(gastoRequest);

        assertNull(gasto.getId());
        assertEquals(gastoRequest.valor(), gasto.getValor());
        assertEquals(gastoRequest.formaPagamento(), gasto.getFormaPagamento());
        assertEquals(gastoRequest.data(), gasto.getData());
        assertEquals(gastoRequest.local(), gasto.getLocal());
        assertEquals(gastoRequest.descricao(), gasto.getDescricao());
        assertEquals(gastoRequest.necessidade(), gasto.getNecessidade());
        assertEquals(gastoRequest.pessoaId(), gasto.getPessoa().getId());
        assertNull(gasto.getPessoa().getNome());
        assertNull(gasto.getPessoa().getRendaMensal());
        assertNull(gasto.getPessoa().getProfissao());
    }

    @DisplayName("Deve converter Gasto em GastoDTO")
    @Test
    void testToDto() {
        Gasto gasto =
                new Gasto(1, 1550.0, FormaPagamento.DINHEIRO, LocalDate.of(2024, 3, 22),
                        "Brasília", "Geladeira", true,
                        new Pessoa(1, "Pedro", 2000.0, "pintor"));

        GastoDTO gastoDTO = gastoMapper.toDto(gasto);

        assertEquals(gasto.getId(), gastoDTO.id());
        assertEquals(gasto.getValor(), gastoDTO.valor());
        assertEquals(gasto.getFormaPagamento(), gastoDTO.formaPagamento());
        assertEquals(gasto.getData(), gastoDTO.data());
        assertEquals(gasto.getLocal(), gastoDTO.local());
        assertEquals(gasto.getDescricao(), gastoDTO.descricao());
        assertEquals(gasto.getNecessidade(), gastoDTO.necessidade());
        assertEquals(gasto.getPessoa().getId(), gastoDTO.pessoaId());

    }

    @DisplayName("Deve converter List<Gasto> em List<GastoDTO>")
    @Test
    void testToDtoList() {
        Gasto gasto1 =
                new Gasto(1, 1550.0, FormaPagamento.DINHEIRO, LocalDate.of(2024, 3, 22),
                        "Brasília", "Geladeira", true,
                        new Pessoa(1, "Pedro", 2000.0, "pintor"));
        Gasto gasto2 =
                new Gasto(2, 2050.0, FormaPagamento.BOLETO, LocalDate.of(2025, 4, 10),
                        "Goiânia", "Computador", false,
                        new Pessoa(3, "Gustavo", 4000.0, "pedreiro"));
        List<Gasto> gastoList = List.of(gasto1, gasto2);

        List<GastoDTO> gastoDTOList = gastoMapper.toDtoList(gastoList);

        for (int i = 0; i < gastoDTOList.size(); i++) {
            assertEquals(gastoList.get(i).getId(), gastoDTOList.get(i).id());
            assertEquals(gastoList.get(i).getValor(), gastoDTOList.get(i).valor());
            assertEquals(gastoList.get(i).getFormaPagamento(), gastoDTOList.get(i).formaPagamento());
            assertEquals(gastoList.get(i).getData(), gastoDTOList.get(i).data());
            assertEquals(gastoList.get(i).getLocal(), gastoDTOList.get(i).local());
            assertEquals(gastoList.get(i).getDescricao(), gastoDTOList.get(i).descricao());
            assertEquals(gastoList.get(i).getNecessidade(), gastoDTOList.get(i).necessidade());
            assertEquals(gastoList.get(i).getPessoa().getId(), gastoDTOList.get(i).pessoaId());
        }
    }
}